import subprocess

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
        # Primul proces, fără stdin
        p = subprocess.Popen(args, stdout=subprocess.PIPE)
    else:
        # Procesele următoare primesc stdin de la procesul anterior
        p = subprocess.Popen(args, stdin=prev_process.stdout, stdout=subprocess.PIPE)
        # Închide stdout-ul procesului anterior pentru a evita deadlock
        prev_process.stdout.close()
    processes.append(p)
    prev_process = p

# Citește output-ul final din ultimul proces
output, error = processes[-1].communicate()

# Afișează rezultatul
print(output.decode())
