#Calculate a grade

#Create a method get_letter_grade that accepts a classroom grade and returns the letter grade as a String. 
#It should return only 'A', 'B', 'C','D', or 'F'.


#get_letter_grade(90) => returns "A"

#get_letter_grade(74) => returns "C"

def getGrade (number) {

	if number < 60
		return 'F'
	elsif number <70
		return 'D'
	elsif number < 80
		return 'C'
	elsif number < 90
		return 'B'
	elsif number < 100
		return 'A'
}
puts getGrade (90)