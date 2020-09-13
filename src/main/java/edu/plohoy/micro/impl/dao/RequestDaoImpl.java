package edu.plohoy.micro.impl.dao;

import edu.plohoy.micro.api.domain.Request;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.UUID;

@Repository
public class RequestDaoImpl {
    private static final String INSERT_QUERY =
            "insert into request (id, person_id, stock_code, stock_count, request_date)" +
                    " values (:id', :personId, :stockCode, :stockCount, :requestDate)";

    private static final String DELETE_QUERY =
            "delete from request where id = :requestId";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RequestDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Request request) {
        jdbcTemplate.update(INSERT_QUERY, Collections.singletonMap("requestId", new BeanPropertySqlParameterSource(request)));
    }

    public void delete(UUID requestId) {
        jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("requestId", requestId));
    }

}
