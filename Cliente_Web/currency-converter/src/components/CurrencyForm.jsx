import React from 'react';

const CurrencyForm = ({ from, to, amount, setFrom, setTo, setAmount, onConvert }) => (
  <div className="form-container">
    <div className="form-group">
      <label>De:</label>
      <select value={from} onChange={(e) => setFrom(e.target.value)}>
        <option value="BRL">Real</option>
        <option value="USD">Dólar Americano</option>
        <option value="EUR">Euro</option>
        <option value="BTC">Bitcoin</option>
        <option value="CHF">Franco-Suiço</option>
        <option value="JPY">Iene Japonês</option>
      </select>
    </div>

    <div className="form-group">
      <label>Para:</label>
      <select value={to} onChange={(e) => setTo(e.target.value)}>
        <option value="BRL">Real</option>
        <option value="USD">Dólar Americano</option>
        <option value="EUR">Euro</option>
        <option value="BTC">Bitcoin</option>
        <option value="JPY">Iene Japonês</option>
      </select>
    </div>

    <div className="form-group">
      <label>Valor:</label>
      <input
        type="number"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
    </div>

    <button onClick={onConvert}>Converter</button>
  </div>
);

export default CurrencyForm;
