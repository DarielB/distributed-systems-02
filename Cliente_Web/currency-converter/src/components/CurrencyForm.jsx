import React from 'react';

const CurrencyForm = ({ from, to, amount, setFrom, setTo, setAmount, onConvert }) => (
  <div className="form-container">
    <div className="form-group">
      <label>De:</label>
      <select value={from} onChange={(e) => setFrom(e.target.value)}>
        <option value="USD">USD</option>
        <option value="EUR">EUR</option>
        <option value="BTC">BTC</option>
      </select>
    </div>

    <div className="form-group">
      <label>Para:</label>
      <select value={to} onChange={(e) => setTo(e.target.value)}>
        <option value="BRL">BRL</option>
        <option value="USD">USD</option>
        <option value="EUR">EUR</option>
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
