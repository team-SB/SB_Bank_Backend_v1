package com.example.sbbank.service.account;

import com.example.sbbank.entity.Transaction;
import com.example.sbbank.entity.account.Account;
import com.example.sbbank.entity.account.AccountRepository;
import com.example.sbbank.entity.account.Record;
import com.example.sbbank.entity.account.RecordRepository;
import com.example.sbbank.entity.member.Member;
import com.example.sbbank.exception.InvalidTokenException;
import com.example.sbbank.exception.UserNotFoundException;
import com.example.sbbank.payload.request.AccountChargeRequestDto;
import com.example.sbbank.payload.request.AccountRegisterRequestDto;
import com.example.sbbank.payload.request.AccountTransferRequestDto;
import com.example.sbbank.payload.response.AccountRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final RecordRepository recordRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountRegisterResponseDto register(AccountRegisterRequestDto request, Member member) {

        if(request.getSecPassword().equals(member.getSecPassword())) {
            throw new InvalidTokenException();
        }

        Random rd = new Random();
        Integer rdAcc = rd.nextInt(999999999) + 111111111;

        Account account = Account.builder()
                .accountNumber(rdAcc)
                .balance(0)
                .member(member)
                .build();

        accountRepository.save(account);

        return new AccountRegisterResponseDto(rdAcc);
    }

    public String transfer(AccountTransferRequestDto request, Member member) {

        if(request.getSecPassword().equals(member.getSecPassword())) {
            throw new InvalidTokenException();
        }

        Record record = Record.builder()
                .target(request.getTarget())
                .money(request.getMoney())
                .transactionType(Transaction.RECEIVE)
                .transactionDate(new Date())
                .balance(0) // 0 아님
                .member(member)
                .build();

        if(request.getMoney() < 0) {
            Transaction type = Transaction.SEND;
            record.setTransactionType(type);
        }

        recordRepository.save(record);
        return "success record";
    }

    public String charge(AccountChargeRequestDto request, Member member) {

        if(request.getSecPassword().equals(member.getSecPassword())) {
            throw new InvalidTokenException();
        }

        Integer setBalance = request.getMoney();

        Account setAccount = accountRepository.findById(member.getId())
                .map(account -> {
                    account.setBalance(account.getBalance() + setBalance);
                    return account;
                })
                .orElseThrow(UserNotFoundException::new);

        accountRepository.save(setAccount);
        return "success charge";
    }

}
