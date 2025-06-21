use std::sync::mpsc::{Receiver, Sender};
use std::sync::mpsc;
use std::thread;

struct AccountMessage {
    command: i32,
    amount: i32,
}

struct Account {
    receiving: Receiver<AccountMessage>,
    balance: i32,
}

impl Account {
    fn new() -> (Self, Sender<AccountMessage>) {
        let (tx, rx) = mpsc::channel();
        let s= Self{
            receiving:rx,
            balance:100
        };
        (s,tx)
    }
    fn run(mut self){
        for received in self.receiving {
           match received.command {
               0 => self.balance += received.amount,
               1 => self.balance -= received.amount,
               _ => {}
           }
        }
        println!("balance = {:?}",self.balance);
    }
}

fn main() {
    let (account,tx) = Account::new();
    let tx1 = tx.clone();
    thread::spawn(move|| {account.run()});
    let one = thread::spawn(move|| {
        for i in 1..20 {
            tx.send(AccountMessage{ command: 0, amount: 1 }).unwrap();
        }
    });
    let two  =thread::spawn(move|| {
        for i in 1..10 {
            tx1.send(AccountMessage{ command: 1, amount: 1 }).unwrap();
        }
    });

    let _ = one.join();
    let _ = two.join();
}