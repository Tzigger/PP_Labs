import asyncio

# definesc corutina
async def worker(name, queue, results):
    while True:
        n = await queue.get()
        if n is None:
            queue.task_done()
            break
        s = 0
        for k in range(0, n+1):
            print(f"Corutina {name}: adun {k} la sumă")
            await asyncio.sleep(0.5)
            s += k
        print(f"Corutina {name}: suma de la 0 la {n} este {s}")
        results[(name, n)] = s
        queue.task_done()

# functia principala async
async def main():
    queue = asyncio.Queue()
    valori_n = [5, 7, 3, 6]
    results = {}
    for n in valori_n:
        await queue.put(n)
    for _ in range(4):
        await queue.put(None)

    workers = [asyncio.create_task(worker(f"W{i+1}", queue, results)) for i in range(4)]
    await queue.join()
    for w in workers:
        await w

    print("\nRezultatele finale:")
    for key, value in results.items():
        print(f"{key[0]} pentru n={key[1]}: suma este {value}")

if __name__ == "__main__":
    asyncio.run(main())
