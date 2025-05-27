from more_itertools import map_reduce
from collections import defaultdict
import re


def map_function(text_input):
    """
    Extracts words from text and returns pairs of (first_letter, word).
    Converts words to lowercase and removes punctuation for consistency.
    """
    # Remove punctuation and split into words
    words = re.findall(r'\b\w+\b', text_input.lower()) 
    for word in words:
        if word: # Ensure word is not empty
            yield (word[0], word)

def reduce_function(key, values):
    """
    Groups words under their common first letter.
    """
    return (key, sorted(list(values))) # Sort words within each group alphabetically

if __name__ == "__main__":
    sample_text = "Ana are mere si pere. Vasile mananca prune si caise. Zoe zice zu."
    print(f"Original text: \"{sample_text}\"\n")

    # Perform map_reduce
    mapped_items = list(map_function(sample_text))
    print(f"Mapped items (first_letter, word):")
    for item in mapped_items:
        print(item)
    print("-" * 30)


    words_only = re.findall(r'\b\w+\b', sample_text.lower())

    # Key function for map_reduce: extracts the first letter
    key_func = lambda word: word[0] if word else ''
    
    # Value function for map_reduce: returns the word itself
    value_func = lambda word: word

    grouped_words = map_reduce(
        words_only,
        keyfunc=key_func,    # Extracts 'a' from 'ana'
        valuefunc=value_func,  # The value is 'ana'
        reducefunc=list      # Collects all words for a key into a list
    )
    
    final_sorted_grouped_result = []
    for letter in sorted(grouped_words.keys()):
        words_for_letter = sorted(grouped_words[letter])
        final_sorted_grouped_result.append((letter, words_for_letter))

    print("\nResult of map_reduce (grouped and sorted words by first letter):")
    for letter, word_list in final_sorted_grouped_result:
        print(f"{letter}: {word_list}")

    print("\nAlternative interpretation (manual map-reduce flow):")
    
    # 1. Map phase (using our defined map_function)
    mapped_pairs = list(map_function(sample_text))
    
    # 2. Shuffle/Group phase (intermediate step, often implicit)
    # Grouping pairs by key (first letter)
    intermediate_groups = defaultdict(list)
    for key, value in mapped_pairs:
        intermediate_groups[key].append(value)
        
    # 3. Reduce phase (using our defined reduce_function logic)
    reduced_result = []
    for key in sorted(intermediate_groups.keys()): # Sort by key (letter)
        reduced_item = reduce_function(key, intermediate_groups[key])
        reduced_result.append(reduced_item)
        
    print("Result from manual map-reduce flow:")
    for letter, word_list in reduced_result:
        print(f"{letter}: {word_list}")

    print("\nFinal chosen implementation (manual map-reduce flow matching problem statement):")

    all_mapped_items = list(map_function(sample_text))
    
    grouped_for_reduce = defaultdict(list)
    for key, value in all_mapped_items:
        grouped_for_reduce[key].append(value)
        
    # Step 3: Reduce

    final_output = []
    # Process in alphabetical order of keys (letters)
    for key_letter in sorted(grouped_for_reduce.keys()):
        # The 'values' for the reduce_function are the words starting with key_letter
        words_for_this_letter = grouped_for_reduce[key_letter]
        # Our reduce_function takes the key and the list of words
        reduced_pair = reduce_function(key_letter, words_for_this_letter)
        final_output.append(reduced_pair)
        
    print(f"Original text: \"{sample_text}\"")
    print("Processed output (sorted by first letter, words within each group sorted):")
    for letter, word_list in final_output:
        print(f"'{letter}': {word_list}")
