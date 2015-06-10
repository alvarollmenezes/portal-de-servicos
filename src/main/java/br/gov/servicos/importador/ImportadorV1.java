package br.gov.servicos.importador;

import br.gov.servicos.servico.*;
import br.gov.servicos.servico.linhaDaVida.LinhaDaVida;
import br.gov.servicos.servico.publicoAlvo.PublicoAlvo;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static br.gov.servicos.foundation.IO.read;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static lombok.AccessLevel.PRIVATE;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.parser.Parser.xmlParser;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@ManagedResource
public class ImportadorV1 {

    ResourcePatternResolver resolver;
    ServicoRepository servicos;

    @Autowired
    public ImportadorV1(ResourcePatternResolver resolver,
                        ServicoRepository servicos) {

        this.resolver = resolver;
        this.servicos = servicos;
    }

    @ManagedOperation
    public Iterable<Servico> importar(File repositorioCartas) throws IOException {
        return this.servicos.save(
                todosOsServicos(repositorioCartas)
                        .parallel()
                        .map(this::toDocument)
                        .map(this::toServico)
                        .collect(toSet()));
    }

    private Stream<Resource> todosOsServicos(File repositorioCartas) throws IOException {
        return Stream.concat(servicosDoRepositorioDeCartas(repositorioCartas), servicosEmbarcados())
                .collect(toMap(Resource::getFilename, Function.identity(), (r1, r2) -> r1))
                .values()
                .stream();
    }

    private Stream<Resource> servicosEmbarcados() throws IOException {
        return Stream.of(resolver.getResources("classpath:v1/**/*.xml"));
    }

    private Stream<Resource> servicosDoRepositorioDeCartas(File repositorioCartas) throws IOException {
        if (!repositorioCartas.exists())
            return Stream.empty();

        return Stream.of(
                resolver.getResources("file://" + repositorioCartas.getAbsolutePath() + "/**/v1/servicos/**/*.xml"));
    }

    @SneakyThrows
    private Document toDocument(Resource resource) {
        log.debug("Importando XML v1: {}", resource);
        return parse(read(resource), resource.getURI().toASCIIString(), xmlParser());
    }

    private Servico toServico(Document doc) {
        doc.outputSettings().prettyPrint(false); // respeita formatação de CDATA

        return new Servico()
                .withId(doc.select("servico > id").text().trim())
                .withTitulo(doc.select("servico > nome").text().trim())
                .withDescricao(doc.select("servico > descricao").html().trim())
                .withResponsavel(
                        new Orgao()
                                .withId(doc.select("orgaoResponsavel > id").text().trim())
                                .withNome(doc.select("orgaoResponsavel > nome").text().trim()))
                .withPrestador(
                        new Orgao()
                                .withId(doc.select("orgaoPrestador > id").text().trim())
                                .withNome(doc.select("orgaoPrestador > nome").text().trim()))
                .withLinhasDaVida(linhasDaVida(doc))
                .withAreasDeInteresse(areasDeInteresse(doc))
                .withPublicosAlvo(publicosAlvo(doc))
                .withCanaisDePrestacao(canaisDePrestacao(doc))
                .withInformacoesUteis(informacoesUteis(doc))
                .withTaxa(doc.select("servico > custoTotalEstimado").text().trim())
                .withUrl(doc.select("servico > url").text().trim())
                .withUrlAgendamento(doc.select("servico > urlAgendamento").text().trim());
    }

    private List<InformacaoUtil> informacoesUteis(Document doc) {
        List<InformacaoUtil> canaisDePrestacao = new ArrayList<>();
        doc.select("servico > informacoesUteis > informacaoUtil")
                .forEach(e -> canaisDePrestacao.add(new InformacaoUtil()
                        .withDescricao(e.select("descricao").text().trim())
                        .withTipo(e.attr("tipo").trim())
                        .withUrl(e.select("link").attr("href").trim())));
        return canaisDePrestacao;
    }

    private List<CanalDePrestacao> canaisDePrestacao(Document doc) {
        List<CanalDePrestacao> canaisDePrestacao = new ArrayList<>();
        doc.select("servico > canaisDePrestacao > canalDePrestacao")
                .forEach(e -> canaisDePrestacao.add(new CanalDePrestacao()
                        .withDescricao(e.select("descricao").text().trim())
                        .withTipo(e.attr("tipo").trim())
                        .withUrl(e.select("link").attr("href").trim())));
        return canaisDePrestacao;
    }

    private List<PublicoAlvo> publicosAlvo(Document doc) {
        List<PublicoAlvo> publicoAlvo = new ArrayList<>();
        doc.select("servico > segmentosDaSociedade > segmentoDaSociedade")
                .forEach(e -> publicoAlvo.add(new PublicoAlvo()
                        .withId(e.select("id").text().trim())
                        .withTitulo(e.select("nome").text().trim())));
        return publicoAlvo;
    }

    private List<LinhaDaVida> linhasDaVida(Document doc) {
        List<LinhaDaVida> linhasDaVida = new ArrayList<>();
        doc.select("servico > eventosDaLinhaDaVida > eventoDaLinhaDaVida")
                .forEach(e -> linhasDaVida.add(new LinhaDaVida()
                        .withId(e.select("id").text().trim())
                        .withTitulo(e.select("nome").text().trim())));
        return linhasDaVida;
    }

    private List<AreaDeInteresse> areasDeInteresse(Document doc) {
        List<AreaDeInteresse> areasDeInteresse = new ArrayList<>();
        doc.select("servico > areasDeInteresse > areaDeInteresse")
                .forEach(e -> areasDeInteresse.add(new AreaDeInteresse()
                        .withId(e.select("id").text().trim())
                        .withTitulo(e.select("nome").text().trim())));
        return areasDeInteresse;
    }
}
