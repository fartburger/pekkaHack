package com.fartburger.fartcheat.command;

public interface ArgumentParser<T> {
    T parse(String argument) throws CommandException;
}
