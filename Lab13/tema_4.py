from functools import reduce

transition_function = lambda state, char: \
    1 if state == 0 and char == 'a' else \
    0 if state == 0 and char == 'b' else \
    0 if state == 0 else \
    1 if state == 1 and char == 'a' else \
    2 if state == 1 and char == 'b' else \
    0 if state == 1 else \
    1 if state == 2 and char == 'a' else \
    0 if state == 2 and char == 'b' else \
    0 if state == 2 else \
    0 

process_string = lambda input_str: \
    reduce(
        transition_function, 
        input_str,           
        0                    
    ) == 2                   # Check if the final state is the accepting state (q2)

if __name__ == "__main__":
    test_strings = {
        "ab": True,
        "cab": True,
        "aab": True,
        "abab": True,
        "aaab": True,
        "bab": True,
        "b": False,
        "a": False,
        "abc": False,
        "aba": False,
        "sometextab": True,
        "sometextaba": False,
        "": False, # Empty string
        "bbaaccaab": True,
        "bbaaccaa": False,
    }

    print("Testing FA that accepts strings ending with 'ab':")
    print("States: q0 (0), q1 (seen 'a'), q2 (seen 'ab' - accepting)")
    print("Transition logic:")
    print("  q0 --a--> q1, q0 --b--> q0, q0 --other--> q0")
    print("  q1 --a--> q1, q1 --b--> q2, q1 --other (not b)--> q0")
    print("  q2 --a--> q1, q2 --b--> q0, q2 --other--> q0")
    print("-" * 30)

    all_tests_passed = True
    for s, expected in test_strings.items():
        result = process_string(s)
        
        print(f"Input: \"{s}\"")
        print(f"  Expected: {expected}, Got: {result} -> {'PASS' if result == expected else 'FAIL'}")
        if result != expected:
            all_tests_passed = False
            # For debugging a failed case:
            final_state_debug = reduce(transition_function, s, 0)
            print(f"    Debug: Final state for \"{s}\" was {final_state_debug}")
            # Trace states:
            current_state_trace = 0
            trace_log = [f"Start (q{current_state_trace})"]
            for char_trace in s:
                prev_state_trace = current_state_trace
                current_state_trace = transition_function(current_state_trace, char_trace)
                trace_log.append(f" --{char_trace}--> q{current_state_trace}")
            print(f"    Trace: {''.join(trace_log)}")


    print("-" * 30)
    if all_tests_passed:
        print("All tests passed successfully!")
    else:
        print("Some tests FAILED.")

