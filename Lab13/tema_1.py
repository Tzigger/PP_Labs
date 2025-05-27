from functional import seq

# Task 1: Reimplement a data processing example from lab 12 using PyFunctional.
# Assuming a common example:
# 1. Start with a list of numbers.
# 2. Filter out numbers not divisible by 3.
# 3. Square the remaining numbers.
# 4. Sum the results.

def process_data(data):
    result = (
        seq(data)
        .filter(lambda x: x % 3 == 0)
        .map(lambda x: x * x)
        .sum()
    )
    return result

if __name__ == "__main__":
    sample_data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    print(f"Original data: {sample_data}")

    # Using PyFunctional
    result = process_data(sample_data)
    print(f"Processing with PyFunctional:")
    print(f"Numbers divisible by 3: {seq(sample_data).filter(lambda x: x % 3 == 0).list()}")
    print(f"Squared: {seq(sample_data).filter(lambda x: x % 3 == 0).map(lambda x: x*x).list()}")
    print(f"Sum of squares: {result}")

    # For comparison, traditional Python approach
    filtered_numbers = [x for x in sample_data if x % 3 == 0]
    squared_numbers = [x*x for x in filtered_numbers]
    sum_of_squares = sum(squared_numbers)
    print(f"\nTraditional Python approach for comparison:")
    print(f"Numbers divisible by 3: {filtered_numbers}")
    print(f"Squared: {squared_numbers}")
    print(f"Sum of squares: {sum_of_squares}")

    assert result == sum_of_squares
