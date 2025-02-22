import re

def read_file(filename):
    with open(filename, 'r') as file:
        return file.read()

def process_text(text):
    text = re.sub(r'[.,!?;:\'\"()\[\]{}]', '', text)
    text = re.sub(r'\s+', ' ', text).strip()
    text = text.lower()
    return text


def main():
    filename = 'input.txt'
    text = read_file(filename)

    print("Textul initial:\n", text)
    processed_text = process_text(text)
    print("\nTextul procesat:\n", processed_text)


if __name__ == "__main__":
    main()
