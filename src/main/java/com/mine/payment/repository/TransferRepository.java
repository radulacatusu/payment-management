package com.mine.payment.repository;

import com.mine.payment.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @stefanl
 */
public interface TransferRepository extends JpaRepository<Transfer, Long> {

}
