use colored::*;

const ARRAY_SIZE: usize = 10;
static mut LAST_NUMBER: u32 = 0;
static mut LAST_FACTORS: [u32; ARRAY_SIZE] = [0; ARRAY_SIZE];


fn main() {
    for i in 25..50 {
        if i == 30 {
            service(i);
        }
        service(i);
    }
}

fn service(number:u32) {
    let mut factors = [0; ARRAY_SIZE];
    unsafe {
        if LAST_NUMBER == number {
            println!("{}", "Cache hit".green());
            factors = LAST_FACTORS;
            print_result(number, factors);
            return;
        }
    }
    println!("{}", "Cache miss".red());
    factorizer(number, &mut factors);
    print_result(number, factors);
    unsafe {
        LAST_NUMBER = number;
        LAST_FACTORS = factors;
    }
}



fn factorizer(mut number: u32, factors: &mut [u32; ARRAY_SIZE]) {
    let mut counter = 0;
    for i in 2..number + 1 {
        while number % i == 0 {
            factors[counter] = i;
            number /=i;
            counter += 1;
        }
    }
}

fn print_result(number: u32, factors : [u32; ARRAY_SIZE]) {
    print!("{number} = ");
    let mut first = true;
    for element in factors {
        if element != 0 {
            if first {
                first = false;
            } else {
                print!(" * ");
            }
            print!("{element}");
        }
    }
    println!();
}