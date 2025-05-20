use std::sync::{Arc, Mutex};
use std::thread;
use colored::Colorize;

const ARRAY_SIZE: usize = 10;

struct FactorizerService {
    last_number:u64,
    last_factors:[u64;10]
}

impl FactorizerService {
    fn service(m_clone: Arc<Mutex<FactorizerService>>, number: u64) {
        {
            let service = m_clone.lock().unwrap();
            if service.last_number == number {
                println!("{}", "Cache hit".green());
                print_result(number, &service.last_factors);
                return;
            } else {
                println!("{}", "Cache miss".red());
            }
        };

        let mut computed_factors = [0; ARRAY_SIZE];
        FactorizerService::factorizer(number, &mut computed_factors);

        let mut service = m_clone.lock().unwrap();
        service.last_number = number;
        service.last_factors = computed_factors;
        print_result(number, &service.last_factors);
    }

    fn factorizer(mut number: u64, factors: &mut [u64; ARRAY_SIZE]) {
        factors.fill(0);
        let mut counter = 0;
        for i in 2..number + 1 {
            while number % i == 0 {
                factors[counter] = i;
                number /=i;
                counter += 1;
            }
        }
    }
}

fn print_result(number: u64, &factors : &[u64; ARRAY_SIZE]) {
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

fn main() {
    let factorizer = FactorizerService{last_number:0, last_factors:[0;ARRAY_SIZE]};
    let m = Arc::new(Mutex::new(factorizer));
    let mut handles = vec![];

    for i in 25..50 {
        let m_clone = Arc::clone(&m);
        let handle = thread::spawn(move || {
            if i == 30 {
                FactorizerService::service(Arc::clone(&m_clone), i);
            }
            FactorizerService::service(m_clone, i);
        });
        handles.push(handle);
    }
    for handle in handles {
        handle.join().unwrap();
    }
}