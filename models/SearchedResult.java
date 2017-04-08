package com.iappstreat.ixigohackathon.models;

/**
 * Created by verma on 08-04-2017.
 */

public class SearchedResult {

    String text,ct,address,_id,cn,en,rt,st,co,_oid,eid,cid,useNLP,lat,lon;



    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public String get_oid() {
        return _oid;
    }

    public void set_oid(String _oid) {
        this._oid = _oid;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getCn() {
        return cn;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getCo() {
        return co;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return eid;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEn() {
        return en;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLon() {
        return lon;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRt() {
        return rt;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getSt() {
        return st;
    }

    public void setUseNLP(String useNLP) {
        this.useNLP = useNLP;
    }

    public String getUseNLP() {
        return useNLP;
    }
}
