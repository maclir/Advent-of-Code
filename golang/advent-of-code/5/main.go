package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
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

	var output string
	if stage == 1 {
		output = part1(input)
	} else {
		output = part2(input)
	}
	fmt.Println(output)
}

func part2(inputs []string) string {
	stacks := make([][]string, 0)
	sIndex := 0
	var line string
	for sIndex, line = range inputs {
		i := strings.Index(line, "[")
		if i < 0 {
			break
		}
	}

	for lineIndex := sIndex; lineIndex >= 0; lineIndex-- {
		line := inputs[lineIndex]
		upcoming := false
		for pos, char := range line {
			if char == '[' {
				upcoming = true
				continue
			}
			if !upcoming {
				continue
			}
			upcoming = false

			stackIndex := pos / 4
			for {
				if len(stacks)-1 >= stackIndex {
					break
				}
				stacks = append(stacks, make([]string, 0))
			}
			stacks[stackIndex] = append(stacks[stackIndex], string(char))
		}
	}
	for lineIndex := sIndex + 2; lineIndex < len(inputs); lineIndex++ {
		re := regexp.MustCompile("[0-9]+")
		instS := re.FindAllString(inputs[lineIndex], -1)

		var inst = []int{}

		for _, i := range instS {
			j, err := strconv.Atoi(i)
			if err != nil {
				panic(err)
			}
			inst = append(inst, j)
		}

		fmt.Printf("%s \n", inputs[lineIndex])
		fmt.Printf("%s \n", stacks)

		fromIndex := inst[1] - 1
		toIndex := inst[2] - 1

		fromLen := len(stacks[fromIndex])
		elements := stacks[fromIndex][fromLen-inst[0] : fromLen]

		stacks[fromIndex] = stacks[fromIndex][:fromLen-inst[0]]
		stacks[toIndex] = append(stacks[toIndex], elements...)
		for {
			if len(stacks)-1 >= toIndex {
				break
			}
			stacks = append(stacks, make([]string, 0))
		}
		fmt.Printf("%s \n\n", stacks)

	}

	tops := ""
	for _, val := range stacks {
		tops = tops + val[len(val)-1]
	}
	return tops
}

func part1(inputs []string) string {
	stacks := make([][]string, 0)
	sIndex := 0
	var line string
	for sIndex, line = range inputs {
		i := strings.Index(line, "[")
		if i < 0 {
			break
		}
	}

	for lineIndex := sIndex; lineIndex >= 0; lineIndex-- {
		line := inputs[lineIndex]
		upcoming := false
		for pos, char := range line {
			if char == '[' {
				upcoming = true
				continue
			}
			if !upcoming {
				continue
			}
			upcoming = false

			stackIndex := pos / 4
			for {
				if len(stacks)-1 >= stackIndex {
					break
				}
				stacks = append(stacks, make([]string, 0))
			}
			stacks[stackIndex] = append(stacks[stackIndex], string(char))
		}
	}
	for lineIndex := sIndex + 2; lineIndex < len(inputs); lineIndex++ {
		re := regexp.MustCompile("[0-9]+")
		instS := re.FindAllString(inputs[lineIndex], -1)

		var inst = []int{}

		for _, i := range instS {
			j, err := strconv.Atoi(i)
			if err != nil {
				panic(err)
			}
			inst = append(inst, j)
		}

		fmt.Printf("%s \n", inputs[lineIndex])
		fmt.Printf("%s \n", stacks)
		for k := 0; k < inst[0]; k++ {
			fromIndex := inst[1] - 1
			toIndex := inst[2] - 1
			fromLen := len(stacks[fromIndex])
			element := stacks[fromIndex][fromLen-1]

			fmt.Printf("element: %s \n", element)
			for {
				if len(stacks)-1 >= toIndex {
					break
				}
				stacks = append(stacks, make([]string, 0))
			}
			stacks[fromIndex] = stacks[fromIndex][:fromLen-1]
			stacks[toIndex] = append(stacks[toIndex], element)
		}
		fmt.Printf("%s \n\n", stacks)

	}

	tops := ""
	for _, val := range stacks {
		tops = tops + val[len(val)-1]
	}
	return tops
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
