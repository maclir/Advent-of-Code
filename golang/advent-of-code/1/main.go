package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
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
	max := []int{0, 0, 0}
	sum := 0

	for _, v := range inputs {
		i, err := strconv.Atoi(v)
		if len(v) == 0 || err != nil {
			for index, value := range max {
				if sum > value {
					max[index] = sum
					sum = value
				}
			}
			sum = 0
			continue
		}
		sum += i
	}
	for index, value := range max {
		if sum > value {
			max[index] = sum
			sum = value
		}
	}
	tSum := 0

	for _, value := range max {
		tSum += value
	}

	return tSum
}

func firstPart(inputs []string) int {
	max := 0
	sum := 0

	for _, v := range inputs {
		i, err := strconv.Atoi(v)
		if len(v) == 0 || err != nil {
			if sum > max {
				max = sum
			}
			sum = 0
			continue
		}
		sum += i
	}
	if sum > max {
		max = sum
	}

	return max
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
