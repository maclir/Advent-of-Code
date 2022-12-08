package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	test := true
	stage := 1
	stageText := "1st"
	testText := "test"
	for i := 1; i < len(os.Args); i++ {
		if os.Args[i] == "prod" {
			test = false
			testText = "actual"
		}
		if os.Args[i] == "2" {
			stage = 2
			stageText = "2nd"
		}
	}
	fmt.Printf("Running %s part with %s data\n\n", stageText, testText)

	var input []string
	var err error
	if test {
		input, err = readLines("test-input.txt")
	} else {
		input, err = readLines("input.txt")
	}
	if err != nil {
		fmt.Fprintf(os.Stderr, "error: %v\n", err)
		os.Exit(1)
	}

	var output int
	if stage == 1 {
		output = part1(input)
	} else {
		output = part2(input)
	}
	fmt.Println(output)
}

func part2(inputs []string) int {
	counter := 0

	for _, v := range inputs {
		min1, max1, min2, max2 := parseLine(v)

		if min1 >= min2 && min1 <= max2 {
			counter++
			continue
		}
		if max1 <= max2 && max1 >= min2 {
			counter++
			continue
		}
		if min2 >= min1 && min2 <= max1 {
			counter++
			continue
		}
		if max2 <= max1 && max2 >= min1 {
			counter++
		}
	}

	return counter
}

func part1(inputs []string) int {
	counter := 0

	for _, v := range inputs {
		min1, max1, min2, max2 := parseLine(v)

		if min1 <= min2 && max1 >= max2 {
			counter++
			continue
		}
		if min2 <= min1 && max2 >= max1 {
			counter++
		}
	}

	return counter
}

func parseLine(line string) (int, int, int, int) {
	min1 := 0
	max1 := 0
	min2 := 0
	max2 := 0

	i := strings.Index(line, "-")
	min1, _ = strconv.Atoi(line[:i])

	line = line[i+1:]
	i = strings.Index(line, ",")
	max1, _ = strconv.Atoi(line[:i])

	line = line[i+1:]
	i = strings.Index(line, "-")
	min2, _ = strconv.Atoi(line[:i])

	line = line[i+1:]
	max2, _ = strconv.Atoi(line)

	return min1, max1, min2, max2
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
