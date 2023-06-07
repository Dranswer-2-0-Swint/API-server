package com.t3q.dranswer.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


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
