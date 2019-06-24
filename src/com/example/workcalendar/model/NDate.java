package com.example.workcalendar.model;

import org.joda.time.LocalDate;

import java.io.Serializable;

public class NDate implements Serializable {
	private static final long serialVersionUID = -6833045513155189557L;
	
	public LocalDate localDate;//��������
    public Lunar lunar;
    public String solarHoliday;//��������
    public String lunarHoliday;//ũ������
    public String solarTerm;//����


    public NDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public NDate() {
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NDate) {
            NDate date = (NDate) obj;
            return localDate.equals(date.localDate);
        } else {
            return false;
        }
    }
}
