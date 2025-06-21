import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [from, setFrom] = useState('USD');
  const [to, setTo] = useState('BRL');
  const [amount, setAmount] = useState(1);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [history, setHistory] = useState([]);

  const fetchHistory = async () => {
    try {
      const response = await axios.get('http://localhost:9080/v1/currency/history');
      setHistory(response.data);
    } catch (err) {
      console.error(err);
      setError('Erro ao buscar o histórico.');
    }
  };

  const handleConvert = async () => {
    try {
      const response = await axios.post(
        'http://localhost:9080/v1/currency/convert',
        JSON.stringify({ from, to, amount: parseFloat(amount) }),
        { headers: { 'Content-Type': 'application/json' } }
      );
      setResult(response.data);
      setError('');
      fetchHistory(); // Atualiza histórico após conversão
    } catch (err) {
      console.error(err);
      setError('Erro ao converter moedas. Verifique o backend.');
    }
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  return (
    <div className="App">
      <h1>Conversor de Moedas</h1>
      <center>
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

        <button onClick={handleConvert}>Converter</button>
      </center>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {result && (
        <div className="result">
          <h3>Resultado</h3>
          <p>{result.originalAmount} {result.from} = {result.convertedAmount} {result.to}</p>
        </div>
      )}

      <h2 style={{ marginTop: '40px' }}>Histórico de Conversões</h2>
      <table className="history-table">
        <thead>
          <tr>
            <th>De</th>
            <th>Para</th>
            <th>Valor</th>
            <th>Convertido</th>
            <th>Data/Hora</th>
          </tr>
        </thead>
        <tbody>
          {history.map((entry, index) => (
            <tr key={index}>
              <td>{entry.fromCurrency}</td>
              <td>{entry.toCurrency}</td>
              <td>{entry.amount}</td>
              <td>{entry.convertedAmount}</td>
              <td>{new Date(entry.timestamp).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
