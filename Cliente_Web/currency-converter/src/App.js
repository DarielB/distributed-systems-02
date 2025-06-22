import React, { useState, useEffect, useCallback } from 'react';
import './styles/App.css';

import CurrencyForm from './components/CurrencyForm';
import ResultDisplay from './components/ResultDisplay';
import HistoryTable from './components/HistoryTable';
import FormatSelector from './components/FormatSelector';
import DownloadButtons from './components/DownloadButtons';

import { fetchHistory, convertCurrency, deleteExchange, updateTimestamp } from './services/currencyService';

function App() {
  // Estados controlados para campos do formulário
  const [from, setFrom] = useState('USD'); // Moeda de origem
  const [to, setTo] = useState('BRL');     // Moeda de destino
  const [amount, setAmount] = useState(1); // Valor a ser convertido

  // Estado para exibir o resultado da conversão
  const [result, setResult] = useState(null);

  // Estado para mensagens de erro
  const [error, setError] = useState('');

  // Histórico de conversões
  const [history, setHistory] = useState([]);

  // Estado para controlar o formato de resposta: JSON ou XML
  const [formato, setFormato] = React.useState('json');

  // Função para carregar o histórico do back-end
  const loadHistory = useCallback(async () => {
    try {
      const data = await fetchHistory(formato); // Busca o histórico com base no formato
      setHistory(data); // Atualiza o estado do histórico
    } catch (err) {
      setError('Erro ao buscar o histórico.');
    }
  }, [formato]);

  // Função chamada ao converter uma moeda
  const handleConvert = async (formatoSelecionado = 'json') => {
    try {
      const res = await convertCurrency(from, to, amount, formato);
      setResult(res); // Atualiza o resultado da conversão
      setError('');
      await loadHistory(); // Recarrega o histórico após conversão
    } catch (err) {
      setError('Erro ao converter moedas.');
    }
  };

  // Função para deletar uma entrada do histórico
  const handleDelete = async (id) => {
    try {
      await deleteExchange(id);
      await loadHistory(formato); // Atualiza o histórico após a exclusão
    } catch (err) {
      setError('Erro ao excluir entrada.');
    }
  };

  // Função para atualizar a data/hora de uma conversão do histórico
  const handleSaveTimestamp = async (id, newTimestamp) => {
    try {
      await updateTimestamp(id, newTimestamp);
      await loadHistory(formato); // Atualiza o histórico após alteração
    } catch (err) {
      setError('Erro ao atualizar data/hora.');
    }
  };

  // Efeito que carrega o histórico ao montar o componente ou mudar o formato
  useEffect(() => {
    loadHistory();
  }, [loadHistory]);

  // JSX da interface principal
  return (
    <div className="App">
      <center>
        <h1>Conversor de Moedas</h1>

        {/* Seletor de formato (JSON ou XML) */}
        <FormatSelector formato={formato} setFormato={setFormato} />

        {/* Formulário de conversão de moeda */}
        <CurrencyForm
          from={from}
          to={to}
          amount={amount}
          setFrom={setFrom}
          setTo={setTo}
          setAmount={setAmount}
          onConvert={() => handleConvert(formato)}
        />

        {/* Exibição de erros, se houver */}
        {error && <p className="error">{error}</p>}

        {/* Exibição do resultado da conversão */}
        <ResultDisplay result={result} />

        {/* Botões para download (JSON, XML, Protobuf) */}
        <DownloadButtons />

        {/* Tabela com o histórico de conversões */}
        <HistoryTable
          history={history}
          onDelete={handleDelete}
          onSaveTimestamp={handleSaveTimestamp}
        />
      </center>
    </div>
  );
}

export default App;
