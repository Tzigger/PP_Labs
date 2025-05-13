import threading
import queue
from typing import Callable, Iterable, List, Any
import time

class ThreadPool:
    def __init__(self, num_threads: int):
        self.num_threads = num_threads
        self.tasks = queue.Queue()
        self.threads = []
        self.results = []
        self.results_lock = threading.Lock()
        self.stop_event = threading.Event()
        self.active_tasks = 0
        self.active_tasks_lock = threading.Lock()
        self.all_done = threading.Event()

        for _ in range(num_threads):
            t = threading.Thread(target=self.worker)
            t.daemon = True
            t.start()
            self.threads.append(t)

    def worker(self):
        while not self.stop_event.is_set():
            try:
                item = self.tasks.get(timeout=0.1)
            except queue.Empty:
                continue
            if item is None:
                self.tasks.task_done()
                break
            idx, func, arg = item
            result = func(arg)
            with self.results_lock:
                self.results[idx] = result
            with self.active_tasks_lock:
                self.active_tasks -= 1
                if self.active_tasks == 0:
                    self.all_done.set()
            self.tasks.task_done()

    def map(self, func: Callable, iterable: Iterable[Any]) -> List[Any]:
        items = list(iterable)
        n = len(items)
        self.results = [None] * n
        self.all_done.clear()
        with self.active_tasks_lock:
            self.active_tasks = n
        # Load balancing: distribuim elementele cât mai egal
        for idx, arg in enumerate(items):
            self.tasks.put((idx, func, arg))
        # Așteptăm să termine toate taskurile
        self.all_done.wait()
        return self.results

    def join(self):
        self.tasks.join()

    def terminate(self):
        self.stop_event.set()
        # Pune None ca să oprească fiecare thread
        for _ in self.threads:
            self.tasks.put(None)
        for t in self.threads:
            t.join()

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.terminate()



def square(x):
    time.sleep(0.2)
    return x * x

def slow_double(x):
    time.sleep(0.5)
    return 2 * x

if __name__ == "__main__":




    items = list(range(1, 10))
    with ThreadPool(4) as pool:
        print("Calculam patratele pentru:", items)
        results = pool.map(square, items)
        print("Rezultate:", results)

    # Test join si terminate explicit
    pool = ThreadPool(3)

    results = pool.map(slow_double, [10, 20, 30, 40, 50, 60])
    print("Dubluri lente:", results)
    pool.join()
    pool.terminate()
