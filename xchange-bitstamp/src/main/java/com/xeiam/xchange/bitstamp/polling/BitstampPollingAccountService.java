package com.xeiam.xchange.bitstamp.polling;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.bitstamp.api.BitStamp;
import com.xeiam.xchange.bitstamp.api.model.Balance;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.xeiam.xchange.service.BasePollingExchangeService;
import com.xeiam.xchange.service.account.polling.PollingAccountService;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import java.util.Arrays;

/**
 * @author Matija Mazi <br/>
 * @created 1/1/13 6:40 PM
 */
public class BitstampPollingAccountService extends BasePollingExchangeService implements PollingAccountService {
  private BitStamp bitstamp;

  public BitstampPollingAccountService(ExchangeSpecification exchangeSpecification, BitStamp bitstampEndpoint) {
    super(exchangeSpecification);
    bitstamp = bitstampEndpoint;
  }

  @Override
  public AccountInfo getAccountInfo() {
    String userName = getUser();
    Balance balance = bitstamp.getBalance(userName, getPwd());
    AccountInfo accountInfo = new AccountInfo();
    accountInfo.setUsername(userName);
    accountInfo.setWallets(Arrays.asList(
        new Wallet("USD", BigMoney.of(CurrencyUnit.USD, balance.getUsdBalance()))
//        new Wallet("BTC", BigMoney.of(CurrencyUnit.of(Currency.getInstance("BTC")), balance.getBtcBalance()))
    ));
    return accountInfo;
  }

  @Override
  public String withdrawFunds() {
    throw new UnsupportedOperationException("Funds withdrawal not yet implemented.");
  }

  /**
   * This returns the currently set deposit address. It will not generate a new address (ie. repeated calls will return
   * the same address).
   *
   * @param description must be null
   * @param notificationUrl must be null
   */
  @Override
  public String requestBitcoinDepositAddress(String description, String notificationUrl) {
    if (description != null || notificationUrl != null) {
      throw new IllegalArgumentException("Description and notification URL are not supported.");
    }
    return bitstamp.getBitcoinDepositAddress(getUser(), getPwd());
  }

  private String getPwd() {
    return exchangeSpecification.getPassword();
  }

  private String getUser() {
    return exchangeSpecification.getUserName();
  }
}
