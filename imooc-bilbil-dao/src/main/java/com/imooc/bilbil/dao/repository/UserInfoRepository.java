package com.imooc.bilbil.dao.repository;

import com.imooc.bilbil.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, Long> {

    long countByNick(String nickKeyword);

    Page<UserInfo> findByNickOrderByFanCountDesc(String nickKeyword, PageRequest pageRequest);

    Page<UserInfo> findByNickOrderByFanCountAsc(String nickKeyword, PageRequest pageRequest);
}
