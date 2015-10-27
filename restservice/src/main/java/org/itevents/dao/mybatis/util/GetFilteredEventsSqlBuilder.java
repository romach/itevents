package org.itevents.dao.mybatis.util;

import org.apache.ibatis.jdbc.SQL;
import org.itevents.model.Technology;
import org.itevents.model.Filter;
import java.util.Iterator;
import java.util.List;

public class GetFilteredEventsSqlBuilder {

    public String getFilteredEvents(final Filter params) {
        return getFilteredEventsSQL(params).toString() + " LIMIT #{limit} OFFSET #{offset}";
    }

    private SQL getFilteredEventsSQL(final Filter params) {
        return new SQL() {{
            SELECT("*");
            FROM("event e");
            WHERE("e.event_date > NOW()");
            if (params.getMaximumDate() != null) {
                WHERE("e.event_date < #{maximumDate}");
            }
            if (params.getCity() != null) {
                WHERE("city_id = #{city.id}");
            }
            if (params.getCity() == null && (params.getLatitude() != null)) {
                WHERE("ST_DWithin((point)::geography, ST_MakePoint(#{longitude},#{latitude})::geography, #{radius})");
            }
            if (params.getFree() != null) {
                if (params.getFree()) {
                    WHERE("(price IS NULL OR price = 0)");
                } else {
                    WHERE("price > 0");
                }
            }
            if (params.getTechnologies() != null) {
                JOIN(makeJoin(params));
                WHERE("e.id=et.event_id");
            }
            ORDER_BY("event_date");
        }};
    }

    public String getFilteredEventsInDateRangeWithRating(final Filter params) {
        SQL sql =  getFilteredEventsInDateRangeWithRatingSQL(params);
        return sql + " LIMIT #{limit}";

    }

    private SQL getFilteredEventsInDateRangeWithRatingSQL(final Filter params) {
        SQL sql = getFilteredEventsSQL(params);
        sql.LEFT_OUTER_JOIN(" (SELECT event_id, COUNT(*) as visits FROM visit_log " +
                "GROUP BY event_id) AS visits ON e.id = visits.event_id");
        return sql;
    }

    private String makeJoin(Filter params) {
        StringBuilder sb = new StringBuilder();
        sb.append("event_technology et ON ");
        List<Technology> technologies = params.getTechnologies();
        Iterator<Technology> iterator = technologies.iterator();
        while (iterator.hasNext()) {
            sb.append("et.technology_id=");
            sb.append(iterator.next().getId());
            if (iterator.hasNext()) {
                sb.append(" or ");
            }
        }
        return sb.toString();
    }
}