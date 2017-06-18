import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { ModeloExclusivo } from './modelo-exclusivo.model';
import { ModeloExclusivoService } from './modelo-exclusivo.service';

@Component({
    selector: 'jhi-modelo-exclusivo-detail',
    templateUrl: './modelo-exclusivo-detail.component.html'
})
export class ModeloExclusivoDetailComponent implements OnInit, OnDestroy {

    modeloExclusivo: ModeloExclusivo;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private modeloExclusivoService: ModeloExclusivoService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['modeloExclusivo']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInModeloExclusivos();
    }

    load(id) {
        this.modeloExclusivoService.find(id).subscribe((modeloExclusivo) => {
            this.modeloExclusivo = modeloExclusivo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInModeloExclusivos() {
        this.eventSubscriber = this.eventManager.subscribe('modeloExclusivoListModification', (response) => this.load(this.modeloExclusivo.id));
    }
}
