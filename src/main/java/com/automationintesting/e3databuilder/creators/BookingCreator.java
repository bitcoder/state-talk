package com.automationintesting.e3databuilder.creators;

import com.automationintesting.e3databuilder.model.BookingRequestModel;
import com.automationintesting.e3databuilder.sql.InsertSql;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class BookingCreator {

    public static void createBooking(BookingRequestModel bookingPayload) throws SQLException {
        // Connect to DB
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:tcp://localhost:9090/mem:rbp");
        ds.setUser("user");
        ds.setPassword("password");
        Connection connection = ds.getConnection();

        // Query to see if the data has already been created
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT * FROM BOOKINGS WHERE roomid = " + bookingPayload.getRoomid() +
                " AND firstname = '" + bookingPayload.getFirstname() +
                "' AND lastname = '" + bookingPayload.getLastname() +
                "' AND depositpaid = " + bookingPayload.isDepositpaid() +
                " AND checkin = '" + sdf.format(bookingPayload.getBookingdates().getCheckin()) +
                "' AND checkout = '" + sdf.format(bookingPayload.getBookingdates().getCheckout()) +"';";

        ResultSet results = connection.prepareStatement(sql).executeQuery();

        int count = 0;
        while(results.next()) {
            count++;
        }

        // If the data hasn't been created, create it now
        if(count == 0){
            InsertSql insertSql = new InsertSql(connection, bookingPayload);

            PreparedStatement createPs = insertSql.getPreparedStatement();

            createPs.executeUpdate();
        }
    }

}
