import subprocess

def main():
    # Citește comanda de la tastatură
    cmd_line = input("Introduceți o comandă cu pipe-uri (|): ")

    # Împarte comanda după '|'
    commands = [cmd.strip() for cmd in cmd_line.split('|')]

    # Construiește pipeline-ul
    prev_process = None
    processes = []
    for i, cmd in enumerate(commands):
        args = cmd.split()
        if i == 0:
            p = subprocess.Popen(args, stdout=subprocess.PIPE)
        else:
            p = subprocess.Popen(args, stdin=prev_process.stdout, stdout=subprocess.PIPE)
            prev_process.stdout.close()
        processes.append(p)
        prev_process = p

    # Citește output-ul final din ultimul proces
    output, error = processes[-1].communicate()

    # Afișează rezultatul
    print(output.decode())

if __name__ == "__main__":
    main()
