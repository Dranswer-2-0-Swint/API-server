package com.t3q.dranswer.entity;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Data
@Table(name = "tb_api_log", schema = "swint", catalog = "svc_plf")
public class LogEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seq_id;
    String req_id;
    String req_user;
    String req_body;
    LocalDateTime req_dt;
    String req_uri;
    String req_md;

    String res_user;
    String res_body;
    LocalDateTime res_dt;
    String res_msg;
    int res_status;

}
