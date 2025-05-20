use std::sync::{Arc, Mutex};
use std::thread;

enum Command {
    Deposit,
    Withdraw,
}


struct Account {
    balance: i32,
}

impl Account {
    fn deposit(&mut self, amount:i32){
        self.balance += amount
    }

    fn withdraw(&mut self, amount:i32){
        self.balance -= amount
    }
}

fn execute (command:Command, account: &mut Account, amount:i32){
    match command {
        Command::Deposit => account.deposit(amount),
        Command::Withdraw => account.withdraw(amount),
    }
}

fn main() {
    let account_global = Account{balance:5};
    let m = Arc::new(Mutex::new(account_global));
    let mut handles = vec![];

    for i in 0..250 {
        let command= if i%2==0 {Command::Withdraw} else { Command::Deposit };
        let m_clone = Arc::clone(&m);
        let handle = thread::spawn(move || {
            let mut account = m_clone.lock().unwrap();
            execute(command,&mut account,1);
        });
        handles.push(handle);
    }
    for handle in handles {
        handle.join().unwrap();
    }

    let final_balance = m.lock().unwrap().balance;
    println!("Final balance: {}", final_balance);
}