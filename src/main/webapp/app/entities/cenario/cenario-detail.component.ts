import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Cenario } from './cenario.model';
import { CenarioService } from './cenario.service';

@Component({
    selector: 'jhi-cenario-detail',
    templateUrl: './cenario-detail.component.html'
})
export class CenarioDetailComponent implements OnInit, OnDestroy {

    cenario: Cenario;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private cenarioService: CenarioService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['cenario']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCenarios();
    }

    load(id) {
        this.cenarioService.find(id).subscribe((cenario) => {
            this.cenario = cenario;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCenarios() {
        this.eventSubscriber = this.eventManager.subscribe('cenarioListModification', (response) => this.load(this.cenario.id));
    }
}
