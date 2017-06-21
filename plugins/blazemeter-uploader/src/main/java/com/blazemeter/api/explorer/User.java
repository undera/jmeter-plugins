package com.blazemeter.api.explorer;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity {

    public User(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report) {
        super(notifier, address, dataAddress, report, "userId", "name");
    }

    public List<Account> getAccounts() throws IOException {
        String uri = address + "/api/v4/accounts";
        JSONObject response = queryObject(createGet(uri), 200);
        return extractAccounts(response.getJSONArray("result"));
    }

    private List<Account> extractAccounts(JSONArray result) {
        List<Account> accounts = new ArrayList<>();

        for (Object obj : result) {
            accounts.add(convertToAccount((JSONObject) obj));
        }

        return accounts;
    }

    private Account convertToAccount(JSONObject obj) {
        return new Account(notifier, address, dataAddress, report, obj.getString("id"), obj.getString("name"));
    }

}
