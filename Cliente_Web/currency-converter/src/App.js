import React, { useState, useEffect } from 'react';
import './styles/App.css';

import CurrencyForm from './components/CurrencyForm';
import ResultDisplay from './components/ResultDisplay';
import HistoryTable from './components/HistoryTable';

import { fetchHistory, convertCurrency, deleteExchange } from './services/currencyService';

function App() {
  const [from, setFrom] = useState('USD');
  const [to, setTo] = useState('BRL');
  const [amount, setAmount] = useState(1);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [history, setHistory] = useState([]);

  const loadHistory = async () => {
    try {
      const data = await fetchHistory();
      setHistory(data);
    } catch (err) {
      setError('Erro ao buscar o histórico.');
    }
  };

  const handleConvert = async () => {
    try {
      const res = await convertCurrency(from, to, amount);
      setResult(res);
      setError('');
      await loadHistory();
    } catch (err) {
      setError('Erro ao converter moedas.');
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteExchange(id);
      await loadHistory();
    } catch (err) {
      setError('Erro ao excluir entrada.');
    }
  };

  useEffect(() => {
    loadHistory();
  }, []);

  return (
    <div className="App">
      <h1>Conversor de Moedas</h1>
      <CurrencyForm
        from={from}
        to={to}
        amount={amount}
        setFrom={setFrom}
        setTo={setTo}
        setAmount={setAmount}
        onConvert={handleConvert}
      />
      {error && <p className="error">{error}</p>}
      <ResultDisplay result={result} />
      <HistoryTable history={history} onDelete={handleDelete} />
    </div>
  );
}

export default App;
