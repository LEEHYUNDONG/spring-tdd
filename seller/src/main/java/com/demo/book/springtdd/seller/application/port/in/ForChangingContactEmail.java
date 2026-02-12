package com.demo.book.springtdd.seller.application.port.in;

import com.demo.book.springtdd.seller.application.port.in.command.ChangeContactEmailCommand;

public interface ForChangingContactEmail {

    void changeContactEmail(ChangeContactEmailCommand command);
}
