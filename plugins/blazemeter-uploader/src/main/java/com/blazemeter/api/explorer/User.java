package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User extends HttpBaseEntity {

    public User(HttpBaseEntity entity) {
        super(entity);
    }

    /**
     * @return list of Account for user token
     */
    public List<Account> getAccounts() throws IOException {
        String uri = address + "/api/v4/accounts";
        JSONObject response = queryObject(createGet(uri), 200);
        return extractAccounts(response.getJSONArray("result"));
    }

    private List<Account> extractAccounts(JSONArray result) {
        List<Account> accounts = new ArrayList<>();

        for (Object obj : result) {
            accounts.add(Account.fromJSON(this, (JSONObject) obj));
        }

        return accounts;
    }
}
