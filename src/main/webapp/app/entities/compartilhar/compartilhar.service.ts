import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { EventManager } from 'ng-jhipster';
import { Compartilhar } from './compartilhar.model';
@Injectable()
export class CompartilharService {

    private resourceUrl = 'api/compartilhars';
    private resourceSearchUrl = 'api/_search/compartilhars';

    constructor(
        private http: Http,
    private eventManager: EventManager,
) { }

    create(compartilhar: Compartilhar): Observable<Compartilhar> {
        const copy: Compartilhar = Object.assign({}, compartilhar);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            this.eventManager
                .broadcast({ name: 'compartilharListModification', content: 'OK'});
            return res.json();
        });
    }

    update(compartilhar: Compartilhar): Observable<Compartilhar> {
        const copy: Compartilhar = Object.assign({}, compartilhar);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            this.eventManager
                .broadcast({ name: 'compartilharListModification', content: 'OK'});
            return res.json();
        });
    }

    find(id: number): Observable<Compartilhar> {
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
        return this.http.delete(`${this.resourceUrl}/${id}`)
            .map(
                (data) => {
                    this.eventManager
                        .broadcast({ name: 'compartilharListModification', content: 'OK'});
                    return data;
                }
            );
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

    public receberCompartilhamento(compartilhar: Compartilhar, data: string): Observable<Compartilhar> {
       return this.http
            .get(`${this.resourceUrl}/aceitar/${compartilhar.id}?conteudo=${data}`)
            .map((res) => res.json());
    }
}
