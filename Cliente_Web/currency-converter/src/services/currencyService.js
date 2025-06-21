import axios from 'axios';

const API_BASE = 'http://localhost:9080/v1/currency';

export const fetchHistory = async () => {
  const response = await axios.get(`${API_BASE}/history`);
  return response.data;
};

export const convertCurrency = async (from, to, amount) => {
  const response = await axios.post(
    `${API_BASE}/convert`,
    JSON.stringify({ from, to, amount: parseFloat(amount) }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  return response.data;
};

export const deleteExchange = async (id) => {
  await axios.delete(`${API_BASE}/delete-exchange`, {
    params: { idExchange: id }
  });
};
