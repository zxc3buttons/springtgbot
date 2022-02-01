package com.example.springtgbot.repository;

import com.example.springtgbot.model.wallet_changes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface UserRepository extends CrudRepository<wallet_changes, Integer> {

    @Query(value = "SELECT SUM(money) FROM wallet_changes WHERE chatId = ?1")
    int getSum(@Param("chat_id") long chatId);

    @Query(value = "SELECT SUM(money) FROM wallet_changes WHERE chatId = ?1 AND changeType = ?2")
    int getChanges(@Param("chat_id") long chatId, @Param("change_type") String changeType);

    boolean existsByChatId(long chatId);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query(value = "UPDATE wallet_changes set category = ?1 where id IN(SELECT MAX(id) FROM wallet_changes)")
    void setCategoryForLastInsert(@Param("category")String category);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query(value = "UPDATE wallet_changes set money = ?1, chatId =?2, date=?3, changeType=?4" +
            " where id IN(SELECT MAX(id) FROM wallet_changes)")
    void setValuesForLastInsert(@Param("money")int money, @Param("chat_id") Long chatId,
                                    @Param("date") Date date, @Param("change_type") String changeType);
}
