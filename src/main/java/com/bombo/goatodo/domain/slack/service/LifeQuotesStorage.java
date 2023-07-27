package com.bombo.goatodo.domain.slack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class LifeQuotesStorage {

    private LifeQuotesStorage() {

    }

    private static final List<String> lifeQuotes = new ArrayList<>();

    static {
        lifeQuotes.add("우리는 가장 많이 어울리는 다섯 사람의 평균이 된다. -짐 론-");
        lifeQuotes.add("세상에  끈기를 대신할 수 있는 것은 아무것도 없다. 재능은  끈기를 대신할 수 없다. 뛰어난 재능을  갖고도  실패하는 사람은 얼마든지 볼 수 있다.  " +
                "천재도  끈기를 대신할 순 없다." +
                "-제이 밴 앤델의 <영원한 자유기업인>  중-");
        lifeQuotes.add("인생이 행복해지는 비결은 끊임없이 작은선물을 받는 것이다. -아이리스 머독");
        lifeQuotes.add("전문가란 자기 주제에 관해서 저지를 수 있는 모든 잘못을  이미 저지른 사람이다. -N.보르-");
        lifeQuotes.add("매일 1퍼센트의 차이가 3개월을 넘기면 100퍼센트의 차이를 만든다는 사실을 기억하라. -사하 하셰미-");
        lifeQuotes.add("그저 첫 발걸음을 떼면 됩니다. 계단 전체를 올려다볼 필요도 없습니다. 그저 첫 발걸음만 떼면 됩니다. -마틴 루터 킹-");
    }

    public static String randomOf() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(0, lifeQuotes.size());
        return lifeQuotes.get(randomIndex);
    }
}
