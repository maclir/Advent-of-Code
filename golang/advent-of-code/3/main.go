package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	//input, err := readLines("test-input.txt")
	input, err := readLines("input.txt")
	if err != nil {
		fmt.Fprintf(os.Stderr, "error: %v\n", err)
		os.Exit(1)
	}
	//output := firstPart(input)
	output := secondPart(input)
	fmt.Println(output)
}

func secondPart(inputs []string) int {
	sum := 0
	for i := 0; i < len(inputs); i += 3 {
		dic := make(map[rune]bool)
		rs := []rune(inputs[i])
		for _, r := range rs {
			dic[r] = true
		}
		for r, _ := range dic {
			found := false
			for _, sr := range inputs[i+1] {
				if r == sr {
					for _, sr2 := range inputs[i+2] {
						if r == sr2 {
							sum += getCode(r)
							found = true
							break
						}
					}
					if found {
						break
					}
				}
			}
			if found {
				break
			}
		}
	}

	return sum
}

func firstPart(inputs []string) int {
	sum := 0
	for _, v := range inputs {
		nSum := 0
		dic := make(map[rune]bool)
		rs := []rune(v)
		firstHalf := rs[0 : len(rs)/2]
		for _, r := range firstHalf {
			dic[r] = true
		}
		secondHalf := rs[len(rs)/2:]
		for r, _ := range dic {
			for _, sr := range secondHalf {
				if r == sr {
					nSum += getCode(r)
					break
				}
			}
		}
		sum += nSum
	}

	return sum
}

func getCode(input rune) int {
	code := int(input) - 96
	if code <= 0 {
		return code + 58
	}
	return code
}

func readLines(path string) ([]string, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}
	return lines, scanner.Err()
}
