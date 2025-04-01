import sys
import os
import collections
import struct
import argparse


UNICODE_NULL_BYTE_THRESHOLD = 0.30
ASCII_TEXT_BYTE_THRESHOLD = 0.90
ASCII_NON_TEXT_THRESHOLD = 0.10
ASCII_PRINTABLE_WHITESPACE = {9, 10, 13} | set(range(32, 127))
NON_TEXT_BYTES = set(range(0, 9)) | {11, 12} | set(range(14, 32)) | set(range(128, 256))


class GenericFile:
    def get_path(self):
        raise NotImplementedError("Subclasses must implement the 'get_path' method.")

    def get_freq(self):
        raise NotImplementedError("Subclasses must implement the 'get_freq' method.")


class TextASCII(GenericFile):
    # init membri
    def __init__(self, path):
        self.path_absolut = os.path.abspath(path)
        content = None
        try:
            with open(path, 'rb') as f:
                content = f.read()
        except Exception as e:
            print(f"Warning: Read error during TextASCII init for {path}: {e}", file=sys.stderr)

        if content:
            self.frecvente = collections.Counter(content)
        else:
            self.frecvente = collections.Counter()
    # metode
    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente

class TextUNICODE(GenericFile):
    # membri
    def __init__(self, path):
        self.path_absolut = os.path.abspath(path)
        content = None
        try:
            with open(path, 'rb') as f:
                content = f.read()
        except Exception as e:
            print(f"Warning: Read error during TextUNICODE init for {path}: {e}", file=sys.stderr)

        if content:
            self.frecvente = collections.Counter(content)
        else:
            self.frecvente = collections.Counter()

    # metode
    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente

class Binary(GenericFile):
    def __init__(self, path):
        self.path_absolut = os.path.abspath(path)
        content = None
        try:
            with open(path, 'rb') as f:
                content = f.read()
        except Exception as e:
            print(f"Warning: Read error during Binary init for {path}: {e}", file=sys.stderr)

        if content:
            self.frecvente = collections.Counter(content)
        else:
            self.frecvente = collections.Counter()

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente


class XMLFile(TextASCII):
    def __init__(self, path):
        super().__init__(path)

        self.first_tag = None
        content = None
        try:
            with open(path, 'rb') as f:
                content = f.read()
        except Exception as e:
             print(f"Warning: Read error during XMLFile init for tag {path}: {e}", file=sys.stderr)

        if content:
            try:
                start = content.find(b'<') # Pozitia primului < in bytes
                end = content.find(b'>', start) #Pozitia primului > dupa start in bytes
                if start != -1 and end != -1: # daca ambele sunt gasite, decodific utf-8
                    self.first_tag = content[start:end+1].decode('utf-8', errors='ignore')
            except Exception:
                pass
    # mteode - get path si get freq sunt mostenite de la TA
    def get_first_tag(self):
        return self.first_tag

class BMP(Binary): # Inherits from Binary
    def __init__(self, path, width, height, bpp):
        super().__init__(path)

        self.width = width     # Public member
        self.height = height   # Public member
        self.bpp = bpp         # Public member

    def show_info(self):
        return f"Width: {self.width}, Height: {self.height}, BPP: {self.bpp}"


def classify_file(file_path):
    content = None
    try:
        with open(file_path, 'rb') as f:
            content = f.read()
    except Exception as e:
        print(f"Warning: Initial read error for classification {file_path}: {e}", file=sys.stderr)
        return None

    if not content:
        return None

    total_bytes = len(content)
    counts = collections.Counter(content) # frecventa tuturor octetilor

    # 1. UNICODE
    if counts[0] / total_bytes >= UNICODE_NULL_BYTE_THRESHOLD:
        return TextUNICODE(file_path)

    # 2. BMP

    if len(content) >= 30 and content.startswith(b'BM'):
        try:
            # '<' - little-endian, 'i' - integer (4 octeți), 'h' - short (2 octeți).
            width = struct.unpack_from('<i', content, 18)[0]
            height = struct.unpack_from('<i', content, 22)[0]
            bpp = struct.unpack_from('<h', content, 28)[0]
            if width > 0 and height > 0 and bpp > 0:
                 return BMP(file_path, width, height, bpp)
        except struct.error:
             pass

    # 3. ASCII
    printable_count = sum(counts[b] for b in ASCII_PRINTABLE_WHITESPACE if b in counts)
    non_text_count = sum(counts[b] for b in NON_TEXT_BYTES if b in counts)
    is_likely_text = (printable_count / total_bytes >= ASCII_TEXT_BYTE_THRESHOLD and
                      non_text_count / total_bytes <= ASCII_NON_TEXT_THRESHOLD)

    if is_likely_text:
        try:
            start_text = content[:100].decode('utf-8', errors='ignore').strip()
            if start_text.startswith('<?xml'):
                return XMLFile(file_path)
            else:
                 return TextASCII(file_path)
        except Exception:
             return TextASCII(file_path)

    # 4. Default - Binary
    return Binary(file_path)



def main():
    parser = argparse.ArgumentParser(description="Recursively scan a directory and classify files (XML ASCII, UNICODE, BMP)")
    parser.add_argument("directory", help="Target directory path")
    args = parser.parse_args()

    target_dir = args.directory

    if not os.path.isdir(target_dir):
        print(f"Error: '{target_dir}' is not a valid directory.", file=sys.stderr)
        sys.exit(1)

    classified_file_objects = []

    abs_target_dir = os.path.abspath(target_dir)
    print(f"Scanning directory: {abs_target_dir}...")

    for root, _, filenames in os.walk(target_dir):
        for filename in filenames:
            file_path = os.path.join(root, filename)

            if not os.path.isfile(file_path):
                continue

            file_object = classify_file(file_path)

            if file_object:
                classified_file_objects.append(file_object)

    print("\n--- Analysis Results ---")

    xml_files = [f for f in classified_file_objects if isinstance(f, XMLFile)]
    print(f"\nXML ASCII Files ({len(xml_files)}):")
    if xml_files:
        for f in xml_files:
            print(f.path_absolut)
    else:
        print("None found.")

    unicode_files = [f for f in classified_file_objects if isinstance(f, TextUNICODE)]
    print(f"\nUNICODE Files ({len(unicode_files)}):")
    if unicode_files:
        for f in unicode_files:
             print(f.path_absolut)
    else:
        print("None found.")

    bmp_files = [f for f in classified_file_objects if isinstance(f, BMP)]
    print(f"\nBMP Files ({len(bmp_files)}):")
    if bmp_files:
        for f in bmp_files:
             print(f"{f.path_absolut} (Width: {f.width}, Height: {f.height}, BPP: {f.bpp})")

    else:
        print("None found.")

    print("\nScan complete.")

if __name__ == "__main__":
    main()