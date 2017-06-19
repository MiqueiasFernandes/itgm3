import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ModeloExclusivo } from './modelo-exclusivo.model';
import { Modelo } from '../modelo/modelo.model';
import { Cenario } from '../cenario/cenario.model';
@Injectable()
export class ModeloExclusivoService {

    private resourceUrl = 'api/modelo-exclusivos';
    private resourceSearchUrl = 'api/_search/modelo-exclusivos';

    constructor(private http: Http) { }

    create(modeloExclusivo: ModeloExclusivo): Observable<ModeloExclusivo> {
        const copy: ModeloExclusivo = Object.assign({}, modeloExclusivo);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(modeloExclusivo: ModeloExclusivo): Observable<ModeloExclusivo> {
        const copy: ModeloExclusivo = Object.assign({}, modeloExclusivo);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<ModeloExclusivo> {
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

    getAllModelos(): Observable<ModeloExclusivo[]> {
        return this.query({
            page: 0,
            size: 100,
            sort: ['id']
        }).map(
            (res: Response) => {
                const modelosExclusivos: ModeloExclusivo[] = res.json();
                return modelosExclusivos;
            });
    }

    getModeloExclusivoByModelo(modelo: Modelo): Observable<ModeloExclusivo[]> {
        return this.getAllModelos().map(
            (modelosExclusivos: ModeloExclusivo[]) => {
                if (!modelo) {
                    return null;
                }
                const modelos: ModeloExclusivo[] = [];
                modelosExclusivos.forEach((model) => {
                    if (model.modelo.id === modelo.id) {
                        modelos.push(model);
                    }
                });
                return modelos;
            }
        );
    }

    getModeloExclusivoByModeloAndCenario(modelo: Modelo, cenario: Cenario): Observable<ModeloExclusivo[]> {
        return this.getModeloExclusivoByModelo(modelo).map(
            (modelosExclusivos: ModeloExclusivo[]) => {
                if (!cenario) {
                    return null;
                }
                const modelos: ModeloExclusivo[] = [];
                modelosExclusivos.forEach((model) => {
                    if (model.cenario.id === cenario.id) {
                        modelos.push(model);
                    }
                });
                return modelos;
            }
        );
    }
}
