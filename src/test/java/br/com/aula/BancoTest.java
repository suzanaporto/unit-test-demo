package br.com.aula;

import java.util.Arrays;

import br.com.aula.exception.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, ContaNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, ContaNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test(expected = ContaNumeroInvalidoException.class)
	public void naoDeveCadastrarNumeroContaInvalido() throws ContaJaExistenteException, ContaNumeroInvalidoException {
		// Cenario
		Cliente c = new Cliente("Cliente");
		Conta conta_nova = new Conta(c, -29384, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();
		// Ação
		banco.cadastrarConta(conta_nova);

		Assert.fail();
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNomeJaExistente() throws ContaJaExistenteException, ContaNumeroInvalidoException {
		//Cenario
		Cliente cliente = new Cliente("Nome Igual");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Nome Igual");
		Conta conta2 = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco b = new Banco();

		//ação
		b.cadastrarConta(conta1);
		b.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());

		//Verificar se a conta de Origem existe na transferencia
		//assertNotNull(banco.obterContaPorNumero(contaOrigem.getNumeroConta()));
		//Verificar se a conta de Destino existe na transferencia
		//assertNotNull(banco.obterContaPorNumero(contaDestino.getNumeroConta()));

		// Conta de origem, poupança, verificar saldo negativo
//		if (contaOrigem.getTipoConta() == TipoConta.POUPANCA){
//			assertTrue("Conta nao pode ter saldo negativo",contaOrigem.getSaldo() >= 0);
//		}
	}

	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarContaOrigemExistente() throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaNegativaException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaDestino));

		// Acao
		banco.efetuarTransferencia(123,456, 78);

		Assert.fail();
	}

	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarContaDestinoExistente() throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaNegativaException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem));

		// Acao
		banco.efetuarTransferencia(123,456, 78);

		Assert.fail();
	}

	@Test(expected = ContaSemSaldoException.class)
	public void naoDevePermitirContaOrigemPoupancaSaldoNegativo() throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaNegativaException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Acao
		banco.efetuarTransferencia(123,456, 12000);

		Assert.fail();
	}

	@Test(expected = TransferenciaNegativaException.class)
	public void naoPermitirTransferirValorNegativo() throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaNegativaException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Acao
		banco.efetuarTransferencia(123,456, -12000);

		Assert.fail();
	}

}
