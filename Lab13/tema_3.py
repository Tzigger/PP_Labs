from functools import reduce
import operator

if __name__ == "__main__":
    initial_list = [1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8]
    print(f"Initial list: {initial_list}\n")

    # Step 1: Eliminarea oricărui număr < 5
    filtered_list = list(filter(lambda x: x >= 5, initial_list))
    print(f"1. After filtering numbers < 5: {filtered_list}")
    assert filtered_list == [21, 75, 39, 7, 35, 31, 7, 8]

    # Step 2: Gruparea numerelor în perechi
    it = iter(filtered_list)
    paired_list = list(zip(it, it)) 
    print(f"2. After grouping into pairs: {paired_list}")
    assert paired_list == [(21, 75), (39, 7), (35, 31), (7, 8)]

    # Step 3: Multiplicarea numerelor din perechi
    multiplied_pairs = list(map(lambda pair: pair[0] * pair[1], paired_list))
    print(f"3. After multiplying numbers in pairs: {multiplied_pairs}")
    assert multiplied_pairs == [1575, 273, 1085, 56]

    # Step 4: Sumarea rezultatelor
    final_sum = reduce(lambda acc, x: acc + x, multiplied_pairs, 0)
    print(f"4. Sum of the results: {final_sum}")
    assert final_sum == 2989

    print(f"\nFinal result: {final_sum}")
    print("\nChained operations:")
    
    # Step 1
    step1 = filter(lambda x: x >= 5, initial_list)
    
    # Step 2 - needs to be handled carefully with iterators if chained directly
    list_step1 = list(step1)
    it_step1 = iter(list_step1)
    step2 = zip(it_step1, it_step1)
    
    # Step 3
    step3 = map(lambda pair: pair[0] * pair[1], step2)
    
    # Step 4
    step4 = reduce(operator.add, step3, 0)
    
    print(f"Result from chained operations: {step4}")
    assert step4 == 2989
