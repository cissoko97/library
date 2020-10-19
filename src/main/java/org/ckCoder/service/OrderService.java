package org.ckCoder.service;

import org.ckCoder.database.Connexion;
import org.ckCoder.models.Command;
import org.ckCoder.models.jasper.InvoiceClient;
import org.ckCoder.models.Line;
import org.ckCoder.service.contract.IService;
import org.ckCoder.utils.hygratation.CommandHydratation;
import org.ckCoder.utils.pagination.Pagination;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderService implements IService<Command, Long> {
    @Override
    public Command create(Command command) throws SQLException, IOException {
        return null;
    }

    @Override
    public Command update(Command command) throws SQLException {
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Command> findAll(Command command) throws SQLException {
        return null;
    }

    @Override
    public Command findById(Long aLong) {
        return null;
    }

    public Command created(Set<Long> idBooks, Long idUser) throws SQLException {
        StringBuilder idBookStrinify = new StringBuilder();
        int i = 1;
        for (Long id : idBooks) {
            if (i < idBooks.size())
                idBookStrinify.append(id).append(",");
            else
                idBookStrinify.append(id);
        }

        CallableStatement stm = Connexion.getConnection().prepareCall("call save_order_simple_order(?,?)");
        stm.setString(1, idBookStrinify.toString());
        stm.setLong(2, idUser);

        Command command = new Command();
        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            while (res.next())
                command = CommandHydratation.commandHelperHydratation(res);
        }

        return command;
    }

    public Pagination<Command> findAll(int page, int size) throws SQLException {
        /*
         * determination du nombre total de page
         */
        String req = "SELECT COUNT(*) FROM command";
        PreparedStatement preparedStatement = Connexion.getConnection().prepareStatement(req);
        int nombreElement = 0;
        ResultSet res = preparedStatement.executeQuery();
        while (res.next())
            nombreElement = res.getInt(1);

        int nombrePage = (int) Math.ceil((double) nombreElement / size);

        // recupération de commandes
        CallableStatement stm = Connexion.getConnection().prepareCall("call findAll_command(?,?)");
        stm.setInt(1, page);
        stm.setInt(2, size);

        List<Command> commands = new ArrayList<>();

        if (stm.execute()) {
           res = stm.getResultSet();

           while(res.next())
               commands.add(CommandHydratation.commandHelperHydratation(res));

        }

        return new Pagination<>(commands, nombreElement, nombrePage);
    }

    public List<Command> findBetwenTwoDate(LocalDateTime begin, LocalDateTime end) throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call find_command_between_two_date(?,?)");
        stm.setTimestamp(1, Timestamp.valueOf(begin));
        stm.setTimestamp(2, Timestamp.valueOf(end));

        List<Command> commands = new ArrayList<>();

        if (stm.execute()) {
            ResultSet res = stm.getResultSet();

            while(res.next())
                commands.add(CommandHydratation.commandHelperHydratation(res));

        }
        return commands;
    }

    public List<Line> findOrderAndLineOrder(long idCommande) throws SQLException {
        List<Line> lines = new ArrayList<>();
        CallableStatement stm = Connexion.getConnection().prepareCall("call find_command_and_lineItem(?)");
        stm.setLong(1, idCommande);

        if (stm.execute()) {
            ResultSet res = stm.getResultSet();
            while (res.next()) {
                lines.add(CommandHydratation.lineHelperHydratation(res));
            }
        }

        return lines;
    }

    public boolean change_status_command(long id_order) throws SQLException {
        CallableStatement stm = Connexion.getConnection().prepareCall("call change_status_command(?)");
        stm.setLong(1, id_order);
        return stm.execute();
    }

    public List<InvoiceClient> getInvoiceClient() {
        List<InvoiceClient> invoiceClientList = new ArrayList<>();
        try {
            CallableStatement stm = Connexion.getConnection().prepareCall("call invoice_clt()");
            if(stm.execute()){
                ResultSet res = stm.getResultSet();

                while (res.next()) {
                    InvoiceClient invoiceClient = new InvoiceClient();

                    invoiceClient.setBook_price(res.getDouble("book_price"));
                    invoiceClient.setCmd_create(res.getString("cmd_create"));
                    invoiceClient.setUser_name(res.getString("user_name"));
                    invoiceClient.setBook_title(res.getString("book_title"));
                    invoiceClient.setPerson_createdAt(res.getString("person_createdAt"));

                    invoiceClientList.add(invoiceClient);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return invoiceClientList;
    }
}
