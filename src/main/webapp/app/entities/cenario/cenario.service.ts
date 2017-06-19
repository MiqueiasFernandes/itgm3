import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Cenario } from './cenario.model';
import { Projeto } from '../index';

@Injectable()
export class CenarioService {

    private resourceUrl = 'api/cenarios';
    private resourceSearchUrl = 'api/_search/cenarios';

    constructor(private http: Http) { }

    create(cenario: Cenario): Observable<Cenario> {
        const copy: Cenario = Object.assign({}, cenario);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(cenario: Cenario): Observable<Cenario> {
        const copy: Cenario = Object.assign({}, cenario);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Cenario> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
        ;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }

    public getCenariosByProjeto(projeto: Projeto): Observable<Cenario[]> {
        return this.query({
            page: 0,
            size: 100,
            sort: ['id']
        }).map(
            (res: Response) => {
                if (!projeto) {
                    return [];
                }
                const cens: Cenario[] = res.json();
                const cenarios: Cenario[] = [];
                cens.forEach((cenario) => {
                    if (cenario.projeto.id === projeto.id) {
                        cenarios.push(cenario);
                    }
                });
                return cenarios;
            });
    }

    public listFiles(cenario: Cenario): Observable<string> {
        return this.http.get(`${this.resourceUrl}/listar/${cenario.id}`)
            .map((res) => res.json().files);
    }

    public publicarArquivo(cenario: Cenario, path: string, file: string, getText: boolean, isImage: boolean): Observable<any> {
        if (getText) {
            const caminho = cenario.caminho.endsWith('/') ? cenario.caminho : (cenario.caminho + '/');
            return this.http.get(`${this.resourceUrl}/publicar/-1/?path=${path}&file=${caminho + '*/' + file}&meta=false&image=false`)
                .map((res) => res.json()); ////removido .file
        } else {
            return this.http.get(`${this.resourceUrl}/publicar/${cenario.id}/?path=${path}&file=${file}&meta=true&image=${isImage}`)
                .map((res) => res.json()); ////removido .file
        }
    }
}
