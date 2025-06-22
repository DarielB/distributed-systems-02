import axios from 'axios';
import { XMLParser } from 'fast-xml-parser';
import { parseXmlConvertResponse, parseXmlHistory} from './xmlParsers';

const API_BASE = 'http://localhost:9080/v1/currency';
const xmlParser = new XMLParser();

// Buscar histórico
export const fetchHistory = async (formato = 'json') => {
  const response = await axios.get(`${API_BASE}/history?formato=${formato}`, {
    headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
    responseType: formato === 'xml' ? 'text' : 'json',
  });
  
  if (formato === 'xml') {
    const jsonObj = xmlParser.parse(response.data);
    const parsed = parseXmlHistory(jsonObj);
    return parsed;
  }
  return response.data;
};

// Conversão de moeda
export const convertCurrency = async (from, to, amount, formato = 'json') => {
  const response = await axios.post(
    `${API_BASE}/convert?formato=${formato}`,
    { from, to, amount },
    {
      headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
      responseType: formato === 'xml' ? 'text' : 'json',
    }
  );

  return formato === 'xml'
    ? parseXmlConvertResponse(xmlParser.parse(response.data))
    : response.data;
};

// Deletar item do histórico
export const deleteExchange = async (id, formato = 'json') => {
  await axios.delete(`${API_BASE}/delete-exchange?formato=${formato}`, {
    params: { idExchange: id },
    headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
    responseType: formato === 'xml' ? 'text' : 'json',
  });
};

// Atualizar timestamp
export const updateTimestamp = async (id, newTimestamp, formato = 'json') => {
  await axios.put(
    `${API_BASE}/update-timestamp?id=${id}&newTimestamp=${newTimestamp}&formato=${formato}`,
    null,
    {
      headers: { Accept: formato === 'xml' ? 'application/xml' : 'application/json' },
      responseType: formato === 'xml' ? 'text' : 'json',
    }
  );
};

export const downloadJson = () =>
  axios.get(`${API_BASE}/download`, {
    responseType: 'blob',
    headers: { Accept: 'application/json' },
  });

export const downloadXml = () =>
  axios.get(`${API_BASE}/download`, {
    responseType: 'blob',
    headers: { Accept: 'application/xml' },
  });

