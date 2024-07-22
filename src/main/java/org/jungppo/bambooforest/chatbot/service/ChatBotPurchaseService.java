package org.jungppo.bambooforest.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.jungppo.bambooforest.chatbot.domain.ChatBotItem;
import org.jungppo.bambooforest.chatbot.domain.entity.ChatBotPurchaseEntity;
import org.jungppo.bambooforest.chatbot.domain.repository.ChatBotPurchaseRepository;
import org.jungppo.bambooforest.chatbot.dto.ChatBotPurchaseRequest;
import org.jungppo.bambooforest.chatbot.exception.ChatBotNotFoundException;
import org.jungppo.bambooforest.global.oauth2.domain.CustomOAuth2User;
import org.jungppo.bambooforest.member.domain.entity.MemberEntity;
import org.jungppo.bambooforest.member.domain.repository.MemberRepository;
import org.jungppo.bambooforest.member.exception.MemberNotFoundException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatBotPurchaseService {

    private final MemberRepository memberRepository;
    private final ChatBotPurchaseRepository chatBotPurchaseRepository;

    @Retryable(retryFor = {OptimisticLockingFailureException.class})
    @Transactional
    public void purchaseChatBot(final ChatBotPurchaseRequest chatBotPurchaseRequest,
                                final CustomOAuth2User customOAuth2User) {
        final ChatBotItem chatBotItem = ChatBotItem.findByName(chatBotPurchaseRequest.getChatBotItemName())
                .orElseThrow(ChatBotNotFoundException::new);
        final MemberEntity memberEntity = memberRepository.findByIdWithLock(customOAuth2User.getId())
                .orElseThrow(MemberNotFoundException::new);

        memberEntity.subtractBatteries(chatBotItem.getPrice());
        memberEntity.addChatBot(chatBotItem);

        savePurchase(chatBotItem, memberEntity);
    }

    public void savePurchase(final ChatBotItem chatBotItem, final MemberEntity memberEntity) {
        final ChatBotPurchaseEntity purchaseEntity = ChatBotPurchaseEntity.builder()
                .chatBotItem(chatBotItem)
                .member(memberEntity)
                .build();

        chatBotPurchaseRepository.save(purchaseEntity);
    }
}
