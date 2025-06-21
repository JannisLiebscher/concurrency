use std::sync::mpsc::{Receiver, Sender};
use std::sync::mpsc;
use std::thread;

struct AccountMessage {
    command: i32,
    amount: i32,
    sender: Sender<BackchannelMessage>
}

struct BackchannelMessage {
    balance: i32,
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
               0 => { self.balance += received.amount;
               received.sender.send(BackchannelMessage{
                   balance: self.balance
               }).unwrap()},
               1 => { self.balance -= received.amount;
                   received.sender.send(BackchannelMessage{
                       balance: self.balance
                   }).unwrap()},
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
        let (tx_in, rx_in) = mpsc::channel();
        for i in 1..20 {
            tx.send(AccountMessage{ command: 0, amount: 1, sender:tx_in.clone() }).unwrap();
            println!("new balance = {:?}",rx_in.recv().unwrap().balance);

        }
    });
    let two  = thread::spawn(move|| {
        for i in 1..10 {
            let (tx_in, rx_in) = mpsc::channel();
            tx1.send(AccountMessage{ command: 1, amount: 1, sender:tx_in.clone() }).unwrap();
            println!("new balance = {:?}",rx_in.recv().unwrap().balance);
        }
    });

    let _ = one.join();
    let _ = two.join();
}