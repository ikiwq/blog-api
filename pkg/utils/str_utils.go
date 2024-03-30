package strutils

import (
	"log"
	"strconv"
	"strings"
)

func IsBlank(s string) bool {
	return strings.TrimSpace(s) == ""
}

func StrToInt(str string) int {
	intValue, err := strconv.Atoi(str)

	if err == nil {
		return intValue
	}

	log.Printf("Error converting %s to int: %v\n", str, err)
	return 0
}

func StrArrayToIntArray(excludeStrings []string) []int {
	var excludeIntegers []int

	for _, str := range excludeStrings {
		if intValue, err := strconv.Atoi(str); err == nil {
			excludeIntegers = append(excludeIntegers, intValue)
		} else {
			log.Printf("Error converting %s to int: %v\n", str, err)
		}
	}

	return excludeIntegers
}

func StrToIntArray(str string, seprator string) []int {
	elements := strings.Split(str, seprator)

	validElements := make([]int, 0, len(elements))
	for _, element := range elements {
		if num, err := strconv.Atoi(element); err == nil {
			validElements = append(validElements, num)
		}
	}

	return validElements
}

func ValidateArrayAsString(str string, seprator string) string {
	elements := strings.Split(str, seprator)

	validElements := make([]string, 0, len(elements))
	for _, element := range elements {
		if _, err := strconv.Atoi(element); err == nil {
			validElements = append(validElements, element)
		}
	}
	result := strings.Join(validElements, seprator)

	return result
}

func IsValidNum(s string) bool {
	_, err := strconv.Atoi(s)
	return err == nil
}
