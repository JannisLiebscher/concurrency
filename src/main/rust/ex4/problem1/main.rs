const ARRAY_SIZE: usize = 10;
fn main() {
    for i in 25..50 {
        let mut factors = [0; ARRAY_SIZE];
        factorizer(i, &mut factors);
        print_result(i, factors);
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