import axios from 'axios';

const API_BASE = 'http://192.168.11.153:9080/v1/currency';

export const obterHistorico = async () => {
  const response = await axios.get(`${API_BASE}/history`);
  return response.data;
};

export const convertCurrency = async (from:string, to:string, amount:string) => {
  const response = await axios.post(
    `${API_BASE}/convert`,
    JSON.stringify({ from, to, amount: parseFloat(amount) }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  return response.data;
};

export const apagarConversaoo = async (id:number) => {
  await axios.delete(`${API_BASE}/delete-exchange`, {
    params: { idExchange: id }
  });
};

export const atualizarTimestamp = async (id:number, newTimestamp:string) => {
  await axios.put(`${API_BASE}/update-timestamp?id=${id}&newTimestamp=${newTimestamp}`);
};

