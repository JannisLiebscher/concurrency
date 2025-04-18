const ARRAY_SIZE: usize = 10;
fn main() {
    let number: u32 = 150;
    let factors:[u32; ARRAY_SIZE];
    factors = factorizer(number);
    for element in factors {
        print!("{element}, ");
    }
}

fn factorizer(mut number: u32) -> [u32; ARRAY_SIZE] {
    let mut counter = 0;
    let mut factors = [0; ARRAY_SIZE];
    for i in 2..number + 1 {
        while number % i == 0 {
            factors[counter] = i;
            number /=i;
            counter += 1;
        }
    }
    factors
}
fn print_result(number: u32) {}