import React, { useEffect, useState } from 'react';
import { Picker } from '@react-native-picker/picker';
import HistoricoConversoes from '../../components/historyConversions';
import { ScrollView, View, Text, TextInput, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { obterHistorico, convertCurrency, apagarConversaoo, atualizarTimestamp } from '../../services/currencyService';
import DownloadButtons from '../../components/DownloadButtons';
const moedas = ['USD', 'EUR', 'BRL', 'GBP', 'JPY', 'AUD'];
const formatos = ['json', 'xml'];

const ConversorMoedas = () => {

    const [erro, setErro] = useState('');
    const [valor, setValor] = useState('');
    const [resultado, setResultado] = useState('');
    const [moedaOrigem, setMoedaOrigem] = useState('USD');
    const [historico, setHistorico] = useState<any[]>([]);
    const [moedaDestino, setMoedaDestino] = useState('BRL');
    const [formato, setFormato] = React.useState('json');

    const onChanged = (text: string) => {
        // Substitui vírgula por ponto para manter consistência
        const novoTexto = text.replace(',', '.');
        // Expressão regular para permitir apenas números com até duas casas decimais
        const regex = /^\d*\.?\d{0,2}$/;

        if (regex.test(novoTexto) || novoTexto === '') {
            setValor(novoTexto);
        }
    };

    const carregarHistorico = async () => {
        try {
            const data = await obterHistorico(formato);
            setHistorico(data);
        } catch (err) {
            setErro('Erro ao buscar o histórico.');
        }
    };

    const converterValores = async () => {
        try {
            const res = await convertCurrency(moedaOrigem, moedaDestino, valor, formato);
            const strRes = `${res.originalAmount} ${res.from} = ${res.convertedAmount} ${res.to}`;
            setResultado(strRes);
            setErro('');
            await carregarHistorico();
        } catch (err) {
            setErro('Erro ao converter moedas.');
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await apagarConversaoo(id);
            await carregarHistorico();
        } catch (err) {
            setErro('Erro ao excluir entrada.');
        }
    };

    const novoTimestamp = async (id: number, newTimestamp: string) => {
        try {
            await atualizarTimestamp(id, newTimestamp);
            await carregarHistorico(); // recarrega o histórico atualizado do backend
        } catch (err) {
            setErro('Erro ao atualizar data/hora.');
        }
    };


    useEffect(() => {
        carregarHistorico();
    }, []);

    return (
        <ScrollView style={styles.container}>
            <Image
                source={require('../../../assets/logo_ufc.png')}
                style={styles.imagem}
                resizeMode="contain"
            />
            <Text style={styles.titulo}>Conversor de Moedas</Text>

            <View style={styles.secao}>
                <Text style={styles.label}>Formato:</Text>
                <Picker
                    selectedValue={formato}
                    onValueChange={(itemValue) => setFormato(itemValue)}
                    style={styles.picker}
                >
                    {formatos.map((m) => (
                        <Picker.Item key={m} label={m} value={m} />
                    ))}
                </Picker>
            </View>
            <View style={styles.secao}>
                <Text style={styles.label}>De:</Text>
                <Picker
                    selectedValue={moedaOrigem}
                    onValueChange={(itemValue) => setMoedaOrigem(itemValue)}
                    style={styles.picker}
                >
                    {moedas.map((m) => (
                        <Picker.Item key={m} label={m} value={m} />
                    ))}
                </Picker>
            </View>

            <View style={styles.secao}>
                <Text style={styles.label}>Para:</Text>
                <Picker
                    selectedValue={moedaDestino}
                    onValueChange={(itemValue) => setMoedaDestino(itemValue)}
                    style={styles.picker}
                >
                    {moedas.map((m) => (
                        <Picker.Item key={m} label={m} value={m} />
                    ))}
                </Picker>
            </View>

            <View style={styles.secao}>
                <Text style={styles.label}>Valor:</Text>
                <TextInput
                    style={styles.input}
                    keyboardType="numeric"
                    placeholder="Digite o valor"
                    value={valor}
                    onChangeText={onChanged}
                />
            </View>

            <TouchableOpacity style={styles.botao} onPress={converterValores}>
                <Text style={styles.textoBotao}>Converter</Text>
            </TouchableOpacity>

            {resultado !== '' && (
                <View style={styles.resultado}>
                    <Text style={styles.textoResultado}>{resultado}</Text>
                </View>
            )}

            {erro !== '' && (
                <View style={styles.erro}>
                    <Text style={styles.textoErro}>{erro}</Text>
                </View>
            )}

            <DownloadButtons />
            
            <HistoricoConversoes historico={historico} deletar={handleDelete} salvarTimestamp={novoTimestamp} />
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        backgroundColor: '#fff',
    },
    titulo: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#4472c4',
        marginBottom: 20,
        textAlign: 'center',
    },
    secao: {
        marginBottom: 15,
    },
    label: {
        color: '#4472c4',
        fontSize: 16,
        marginBottom: 5,
    },
    picker: {
        borderWidth: 1,
        borderColor: '#4472c4',
        backgroundColor: '#f2f2f2',
    },
    input: {
        borderWidth: 1,
        borderColor: '#4472c4',
        borderRadius: 5,
        padding: 10,
        backgroundColor: '#f2f2f2',
    },
    botao: {
        backgroundColor: '#4472c4',
        padding: 12,
        borderRadius: 8,
        alignItems: 'center',
        marginVertical: 10,
    },
    textoBotao: {
        color: '#fff',
        fontSize: 16,
        fontWeight: 'bold',
    },
    resultado: {
        backgroundColor: '#4472c4',
        padding: 15,
        borderRadius: 8,
        alignItems: 'center',
        marginBottom: 20,
    },
    erro: {
        padding: 15,
        borderRadius: 8,
        alignItems: 'center',
        marginBottom: 20,
    },
    textoResultado: {
        color: '#fff',
        fontSize: 18,
        fontWeight: 'bold',
    },
    textoErro: {
        color: '#980000',
        fontSize: 18,
        fontWeight: 'bold',
    },
    subtitulo: {
        fontSize: 18,
        color: '#4472c4',
        fontWeight: 'bold',
        marginBottom: 10,
    },
    itemHistorico: {
        backgroundColor: '#f2f2f2',
        padding: 10,
        marginBottom: 5,
        borderRadius: 5,
        borderColor: '#4472c4',
        borderWidth: 1,
    },
    imagem: {
        width: '100%',
        height: 80,
        marginTop: 50,
        marginBottom: 10,
    }
});

export default ConversorMoedas;
